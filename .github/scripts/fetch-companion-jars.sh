#!/usr/bin/env bash
# Скачивает из Modrinth игровые jar зависимостей.
# Аргументы: <каталог> <KFF> <Patchouli NeoForge> <Fabric API> <Fabric Language Kotlin>
#            <Patchouli Fabric> <Cloth Config> <Mod Menu> <Placeholder API>
set -euo pipefail

OUT_DIR="${1:?destination directory}"
KFF_VERSION="${2:?kotlin-for-forge version_number}"
PATCHOULI_VERSION="${3:?patchouli neoforge version_number}"
FABRIC_API_VERSION="${4:?fabric-api version_number}"
FABRIC_KOTLIN_VERSION="${5:?fabric-language-kotlin version_number}"
PATCHOULI_FABRIC_VERSION="${6:?patchouli fabric version_number}"
CLOTH_VERSION="${7:?cloth-config version_number}"
MODMENU_VERSION="${8:?modmenu version_number}"
PLACEHOLDER_VERSION="${9:?placeholder-api version_number}"
USER_AGENT="${MODRINTH_USER_AGENT:-Nergan/patchouli-button-neoforge-port-mod (https://github.com/Nergan/patchouli-button-neoforge-port-mod)}"

mkdir -p "${OUT_DIR}"

# GitHub-hosted runners share a Modrinth quota, so a burst of release jobs gets HTTP 429.
curl_modrinth() {
  local outfile="" url="" tmp hdr attempt=1 wait=5 code retry_after
  while [[ $# -gt 0 ]]; do
    case "$1" in
      -o)
        outfile="$2"
        shift 2
        ;;
      *)
        url="$1"
        shift
        ;;
    esac
  done
  if [[ -z "${url}" ]]; then
    echo "curl_modrinth: missing URL" >&2
    return 1
  fi
  tmp="$(mktemp)"
  hdr="$(mktemp)"
  while [[ "${attempt}" -le 8 ]]; do
    code="$(curl -sS -L -A "${USER_AGENT}" -D "${hdr}" -o "${tmp}" -w "%{http_code}" "${url}")" || {
      echo "curl failed for ${url}" >&2
      rm -f "${tmp}" "${hdr}"
      return 1
    }
    if [[ "${code}" == "200" ]]; then
      if [[ -n "${outfile}" ]]; then
        mv "${tmp}" "${outfile}"
      else
        cat "${tmp}"
        rm -f "${tmp}"
      fi
      rm -f "${hdr}"
      return 0
    fi
    if [[ "${code}" != "429" ]]; then
      echo "Modrinth HTTP ${code} for ${url}" >&2
      head -c 300 "${tmp}" >&2 || true
      echo >&2
      rm -f "${tmp}" "${hdr}"
      return 22
    fi
    retry_after="$(awk 'tolower($1)=="retry-after:" { gsub("\r","",$2); print $2; exit }' "${hdr}")"
    if [[ "${retry_after}" =~ ^[0-9]+$ ]]; then
      wait="${retry_after}"
    fi
    echo "Modrinth rate limit for ${url}, retry ${attempt}/8 in ${wait}s" >&2
    sleep "${wait}"
    attempt=$((attempt + 1))
    if [[ "${wait}" -lt 60 ]]; then
      wait=$((wait * 2))
    fi
  done
  echo "Modrinth rate limit persisted for ${url}" >&2
  rm -f "${tmp}" "${hdr}"
  return 22
}

fetch_modrinth() {
  local slug="$1"
  local version="$2"
  local json url filename sha512

  json="$(curl_modrinth "https://api.modrinth.com/v2/project/${slug}/version")"
  url="$(echo "${json}" | jq -r --arg v "${version}" '
    first(.[] | select(.version_number == $v) | .files[] | select(.primary) | .url) // empty
  ')"
  filename="$(echo "${json}" | jq -r --arg v "${version}" '
    first(.[] | select(.version_number == $v) | .files[] | select(.primary) | .filename) // empty
  ')"
  sha512="$(echo "${json}" | jq -r --arg v "${version}" '
    first(.[] | select(.version_number == $v) | .files[] | select(.primary) | .hashes.sha512) // empty
  ')"

  if [[ -z "${url}" || -z "${filename}" || -z "${sha512}" ]]; then
    echo "No primary Modrinth file for ${slug} version ${version}" >&2
    exit 1
  fi

  curl_modrinth -o "${OUT_DIR}/${filename}" "${url}"
  echo "${sha512}  ${OUT_DIR}/${filename}" | sha512sum -c -
}

fetch_modrinth "kotlin-for-forge" "${KFF_VERSION}"
fetch_modrinth "patchouli" "${PATCHOULI_VERSION}"
fetch_modrinth "fabric-api" "${FABRIC_API_VERSION}"
fetch_modrinth "fabric-language-kotlin" "${FABRIC_KOTLIN_VERSION}"
fetch_modrinth "patchouli" "${PATCHOULI_FABRIC_VERSION}"
fetch_modrinth "cloth-config" "${CLOTH_VERSION}"
fetch_modrinth "modmenu" "${MODMENU_VERSION}"
fetch_modrinth "placeholder-api" "${PLACEHOLDER_VERSION}"
