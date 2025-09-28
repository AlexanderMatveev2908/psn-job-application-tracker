from argparse import Namespace
import sys

from java_pkg_cli.lib.gradle_pkg.conf_gradle import GradleConf
from java_pkg_cli.lib.reg import REG_LIB


def to_toml_alias(name: str) -> str:
    return name.replace("-", "_").replace(".", "_")


def to_gradle_alias(name: str) -> str:
    parts = to_toml_alias(name).split("_")

    return parts[0].lower() + "".join(word.capitalize() for word in parts[1:])


def err(msg: str) -> None:
    print(f"❌ {msg}")
    sys.exit(1)


def are_args_ok(args: Namespace) -> None:
    if not REG_LIB.fullmatch(args.lib):
        err("invalid lib")

    try:
        GradleConf.from_short(args.config)
    except Exception:
        err("invalid conf gradle")
