gwd() {
  local root_dir

  root_dir=$(basename "$PWD")

  if [[ "$root_dir" == "server" || "$root_dir" == "client" ]]; then
    root_dir=$(realpath "$PWD/../..")
  else
    root_dir=$PWD
  fi

  local parsed=${(L)$(basename "$root_dir")}

  print "$parsed"
}

acw() {
  local root_dir
  root_dir=$(gwd)

  local workspace

  if [[ $1 == '0' ]]; then
    workspace='server'
  elif [[ $1 == '1' ]]; then
    workspace='client'
  else
    echo "invalid arg"
    return 1
  fi

  print "$root_dir-$workspace"
}