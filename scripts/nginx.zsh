ngx() {
  local env="dev"
  [[ "$1" == "k" ]] && env="kind"

  local target="/etc/nginx/env/${env}.conf"
  local active="/etc/nginx/env/active.conf"

  sudo ln -sf "$target" "$active"

  if sudo nginx -t; then
    if systemctl is-active --quiet nginx; then
      echo "♻️  Reloading nginx with $env config..."
      sudo systemctl reload nginx
    else
      echo "🚀 Starting nginx with $env config..."
      sudo systemctl start nginx
    fi
  else
    echo "❌ Config error, not reloading"
  fi
}