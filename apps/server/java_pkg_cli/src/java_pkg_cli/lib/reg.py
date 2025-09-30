import re


REG_VERSION = re.compile(r"^[A-Za-z0-9_.+-]+$")

REG_LIB = re.compile(
    r"^[A-Za-z0-9_.-]+:[A-Za-z0-9_.-]+(?::[A-Za-z0-9_.+-]+)?$"
)
