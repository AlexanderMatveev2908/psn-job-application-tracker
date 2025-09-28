from argparse import Namespace


def split_pkg(args: Namespace) -> tuple[str, str]:
    try:
        group, artifact = args.lib.split(":")

        return group, artifact
    except Exception:
        raise Exception("pkg must be in format group:artifact")


def to_alias(name: str) -> str:
    return name.replace("-", "_").replace(".", "_")
