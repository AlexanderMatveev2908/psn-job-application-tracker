const data = pm.response.json();
if (data?.accessToken) {
  pm.environment.set("access_token", data.accessToken);
}
if (data?.cbcHmacToken) {
  pm.environment.set("cbc_hmac_token", data.cbcHmacToken);
}
