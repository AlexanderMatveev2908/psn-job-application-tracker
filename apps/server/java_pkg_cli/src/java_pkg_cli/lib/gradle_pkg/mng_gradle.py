from argparse import Namespace
from pathlib import Path
import re
from typing import cast

from java_pkg_cli.lib.etc import split_pkg, to_alias
from java_pkg_cli.lib.gradle_pkg.conf_gradle import GradleConf


def add_gradle(p: Path, args: Namespace) -> None:
    content = p.read_text()

    pattern = re.compile(
        r"//_s\s*dependencies\s*\{([\s\S]*?)\}\s*//_e",
        re.MULTILINE,
    )
    match = pattern.search(content)

    if match is None:
        raise Exception("markers _s or _e has not been found")

    pkg = []

    for l in match.group(1).split("\n"):
        stripped = l.strip()
        if stripped:
            pkg.append(stripped)

    type_pkg = cast(GradleConf, args.config).value

    *_, artifact = split_pkg(args)
    alias = to_alias(artifact)

    pkg.append(f"{type_pkg}(libs.{alias})")

    new_block = (f"\n{' ' * 4}").join(pkg)
    new_content = pattern.sub(
        f"//_s\ndependencies {{\n{new_block}\n}}\n//_e",
        content,
    )

    p.write_text(new_content)
