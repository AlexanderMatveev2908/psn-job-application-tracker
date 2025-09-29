import re


REG_VERSION = re.compile(r"^\d+\.\d+\.\d+(?:-[a-zA-Z0-9_.]+)?$")

REG_LIB = re.compile(
    r"^[A-Za-z0-9_.-]+:[A-Za-z0-9_.-]+(?::\d+\.\d+\.\d+(?:-[A-Za-z0-9_.-]+)?)?$"
)
