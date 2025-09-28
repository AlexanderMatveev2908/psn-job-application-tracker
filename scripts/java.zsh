ja(){
    local cwd=$(basename $PWD)

   (
    if [[ "$cwd" != "server" ]]; then
        cd "apps/server/java_pkg_cli" || {echo "❌ pkg_cli not found"; return 1;}
    else
        cd "java_pkg_cli" || {echo "❌ pkg_cli not found"; return 1;}
    fi

     poetry run python -m java_pkg_cli "$@"
   )
}