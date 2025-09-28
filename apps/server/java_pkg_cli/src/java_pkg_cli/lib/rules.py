import argparse
from java_pkg_cli.lib.gradle_pkg.conf_gradle import GradleConf


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description="add pkg to catalog")

    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("-l", "--lib", help="lib format group:artifact")
    group.add_argument("-p", "--plugin", help="plugin id")

    parser.add_argument("-v", "--version", help="version", required=False)
    parser.add_argument(
        "-c",
        "--config",
        type=lambda x: GradleConf.from_short(x),
        default=GradleConf.I,
        choices=list(GradleConf),
        help="gradle conf (short form => i, c, r, ti, tc, tr, a, api)",
    )

    parser.add_argument(
        "--force",
        action="store_true",
        help="override existing version if already in catalog",
    )

    return parser
