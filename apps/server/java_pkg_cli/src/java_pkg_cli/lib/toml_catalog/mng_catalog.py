from argparse import Namespace
from typing import cast
from tomlkit import string, inline_table
from tomlkit.items import InlineTable

from java_pkg_cli.lib.reg import REG_VERSION
from java_pkg_cli.lib.toml_catalog.ctx_catalog import CtxCatalog
from java_pkg_cli.lib.etc import to_gradle_alias, to_toml_alias


def add_catalog(args: Namespace, ctx: CtxCatalog) -> None:
    splitted: list[str] = args.lib.split(":")
    toml_alias = to_toml_alias(splitted[1])
    gradle_alias = to_gradle_alias(splitted[1])

    if gradle_alias in ctx.libs:
        old_row = cast(InlineTable, ctx.libs[gradle_alias])

        if 'ref' in old_row.get("version", {}):
            ctx.versions.pop(toml_alias, None)

            print(f"♻️ overriding existing version for => {gradle_alias}")

    row = inline_table()
    row["module"] = f"{splitted[0]}:{splitted[1]}"

    if len(splitted) > 2 and REG_VERSION.fullmatch(version := splitted[2]):
        ctx.versions[toml_alias] = string(version)
        row["version.ref"] = toml_alias

    ctx.libs[gradle_alias] = row

    print("🗃️ toml updated")
