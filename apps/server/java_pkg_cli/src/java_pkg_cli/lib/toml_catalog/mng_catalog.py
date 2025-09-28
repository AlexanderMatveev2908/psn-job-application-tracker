from argparse import Namespace
from tomlkit import string, inline_table

from java_pkg_cli.lib.reg import REG_VERSION
from java_pkg_cli.lib.toml_catalog.ctx_catalog import CtxCatalog
from java_pkg_cli.lib.etc import to_alias


def add_catalog(args: Namespace, ctx: CtxCatalog) -> None:
    splitted: list[str] = args.lib.split(":")
    alias = to_alias(splitted[1])

    row = inline_table()
    row["module"] = f"{splitted[0]}:{splitted[1]}"

    if len(splitted) > 2 and REG_VERSION.fullmatch(version := splitted[2]):
        ctx.versions[alias] = string(version)
        row["version.ref"] = alias

    ctx.libs[alias] = row

    print("🗃️ toml updated")
