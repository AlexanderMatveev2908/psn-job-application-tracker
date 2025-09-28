from argparse import Namespace
from tomlkit import string, inline_table

from java_pkg_cli.lib.toml_catalog.ctx_catalog import CtxCatalog
from java_pkg_cli.lib.etc import split_pkg, to_alias


def add_catalog(args: Namespace, ctx: CtxCatalog) -> None:
    if args.lib:

        group, artifact = split_pkg(args)
        alias = to_alias(artifact)

        row = inline_table()
        row["module"] = f"{group}:{artifact}"

        if args.version:
            ctx.versions[alias] = string(args.version)
            row["version.ref"] = alias

        ctx.libs[alias] = row

    elif args.plugin:
        alias = to_alias(args.plugin)

        row = inline_table()
        row["id"] = args.plugin

        if args.version:
            ctx.versions[alias] = string(args.version)
            row["version.ref"] = alias

        ctx.plugins[alias] = row
