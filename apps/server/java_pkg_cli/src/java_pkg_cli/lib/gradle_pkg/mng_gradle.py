from argparse import Namespace
from pathlib import Path
import re
from typing import cast

from java_pkg_cli.lib.etc import err, to_gradle_alias
from java_pkg_cli.lib.gradle_pkg.conf_gradle import GradleConf


def add_gradle(p: Path, args: Namespace) -> None:
    content = p.read_text()

    pattern = re.compile(
        r"//_s\s*dependencies\s*\{([\s\S]*?)\}\s*//_e",
        re.MULTILINE,
    )
    match = pattern.search(content)

    if match is None:
        err("markers _s or _e has not been found")

    splitted: list[str] = args.lib.split(":")
    gradle_alias = to_gradle_alias(splitted[1])

    type_pkg: str = GradleConf.from_short(args.config)
    new_pkg = f"{type_pkg}(libs.{gradle_alias})"

    pkg = []

    for l in cast(re.Match[str], match).group(1).split("\n"):
        stripped = l.strip()
        if stripped and new_pkg not in stripped:
            pkg.append(stripped)

    pkg.append(new_pkg)

    new_block = (f"\n{' ' * 4}").join(pkg)
    new_content = pattern.sub(
        f"//_s\ndependencies {{\n{new_block}\n}}\n//_e",
        content,
    )

    p.write_text(new_content)

    print("🛠️ gradle updated")
