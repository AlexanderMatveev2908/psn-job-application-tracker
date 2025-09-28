calc_path_k() {
    local arg=$1       
    local base=$2      
    local p=$(basename "$PWD")
    local target

    if [[ $arg == "0" ]]; then
        target="server"
    elif [[ $arg == "1" ]]; then
        target="client"
    else
        echo "❌ Must specify 0 (server) or 1 (client)"
        return 1
    fi

    if [[ $p == "$target" ]]; then
        echo "${base}.yml"
    else
        echo "apps/${target}/${base}.yml"
    fi
}

kacw() {
    local arg=$1

    if [[ $arg != '0' && $arg != '1' ]]; then
        echo "❌ Usage: kacw 0 (server) | kacw 1 (client)"
        return 1
    fi

    kubectl apply -f "$(calc_path_k "$arg" 'kind-deploy')"
    kubectl apply -f "$(calc_path_k "$arg" 'kind-service')"
}

kac(){
    kubectl apply -f kind-secrets.yml
    kacw 0 & kacw 1
    ngx k
}

kcc(){
    kind create cluster --name $(gwd) --config kind-config.yml &&
    kac
}

