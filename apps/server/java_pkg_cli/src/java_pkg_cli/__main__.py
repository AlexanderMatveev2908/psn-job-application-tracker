from pathlib import Path

from tomlkit import TOMLDocument, parse

from java_pkg_cli.lib.gradle_pkg.mng_gradle import add_gradle
from java_pkg_cli.lib.toml_catalog.ctx_catalog import CtxCatalog
from java_pkg_cli.lib.toml_catalog.mng_catalog import add_catalog
from java_pkg_cli.lib.rules import build_parser

# ? find required dirs
cwd: Path = Path.cwd()
toml_pkg: Path = (cwd / "../gradle/libs.versions.toml").resolve()
gradle_pkg: Path = (cwd / "../app/build.gradle.kts").resolve()

# ! raise if missing
if not toml_pkg.is_file():
    raise FileNotFoundError(f"Missing {toml_pkg}")

if not gradle_pkg.is_file():
    raise FileNotFoundError(f"Missing {gradle_pkg}")

# ? base shape document needed to work
doc: TOMLDocument = parse(toml_pkg.read_text())

# | bkp to be sure
bkp_toml = toml_pkg.with_suffix(".toml.bkp")
bkp_toml.write_text(doc.as_string())

# ? catalog
ctx = CtxCatalog(doc)

parser = build_parser()

args = parser.parse_args()

add_catalog(args, ctx)

toml_pkg.write_text(doc.as_string())

# ? gradle

# | bkp to be sure
bkp_gradle = gradle_pkg.with_suffix(".bkp")
bkp_gradle.write_text(gradle_pkg.read_text())

add_gradle(gradle_pkg, args)

print("☕ pkg updated")
