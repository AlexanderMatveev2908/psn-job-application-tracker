console.log("Main response:", pm.response.json());

if (pm.response.code === 401) {
  pm.sendRequest(
    {
      url: pm.environment.get("DEF_URL") + "/auth/refresh",
      method: "GET",
      header: {
        "Content-Type": "application/json",
      },
    },
    (err, res) => {
      if (err) {
        console.error("☢️ Network error refreshing token", err);
        return;
      }

      if (res.code !== 200) {
        console.error("☢️ Refresh failed", res.code, res.json());
        return;
      }

      const data = res.json();
      if (!data.accessToken) {
        console.error("No access token returned 🥸");
        return;
      }

      pm.environment.set("access_token", data.accessToken);
      console.log("Access token refreshed 🥳");
    }
  );
}
