dbc() {
    dockerfile="apps/client/Dockerfile"
    context="apps/client"

    local tag="<your Docker Hub username>/$(acw 1):latest"

    local build_args=()
    while IFS='=' read -r key value; do
        [[ -z "$key" || "$key" == \#* ]] && continue

        key="${key#"${key%%[![:space:]]*}"}"
        key="${key%"${key##*[![:space:]]}"}"

        value="${value%\"}"
        value="${value#\"}"

        build_args+=( --build-arg "${key}=${value}" )
    done < <(grep -E '^[A-Za-z_][A-Za-z0-9_]*=' .env | sed 's/\r$//')

    docker build \
        --no-cache \
        -f "$dockerfile" \
        -t "$tag" \
        "${build_args[@]}" \
        "$context"

    docker push "$tag"
    ddi "$tag"
}

dbs() {
  local dockerfile="Dockerfile.server"
  local context="."

  local tag="<your Docker Hub username>/$(acw 0):latest"

  docker build \
    --no-cache \
    -f "$dockerfile" \
    -t "$tag" \
    "$context"

  docker push "$tag"
  ddi "$tag" 
}

dsi() {
  local port="${1:-1}"
  local name
  local env_p

  if [[ "$port" == "1" ]]; then
    name="client"
    env_p="apps/client/.env"
  elif [[ "$port" == "0" ]]; then
    name="server"
    env_p="apps/server/.env"
  else
    echo "❌ Unknown port '$port'. Use 1 (client) or 0 (server)"
    return 1
  fi

  local image="<your Docker Hub username>/$(acw "$port"):latest"
  local cname=$(acw "$port")

  docker rm -f "$cname" &>/dev/null || true

  docker run \
    --rm \
    --pull=always \
    --env-file "$env_p" \
    --name "$cname" \
    -p 300${port}:300${port} \
    "$image"

}

