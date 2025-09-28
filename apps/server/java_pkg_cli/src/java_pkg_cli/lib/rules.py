import argparse


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description="add pkg to catalog")

    parser.add_argument("lib", help="lib format group:artifact")
    parser.add_argument(
        "config",
        choices=[
            "i",
            "c",
            "r",
            "a",
            "ti",
            "tc",
            "tr",
            "api",
        ],
        nargs="?",
        default="i",
        help="gradle conf (short form => i, c, r, ti, tc, tr, a, api)",
    )

    return parser
