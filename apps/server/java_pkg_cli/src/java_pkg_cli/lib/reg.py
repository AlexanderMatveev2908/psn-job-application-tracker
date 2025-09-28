import re


REG_VERSION = re.compile(r"^\d+\.\d+\.\d+\-?.*$")
REG_LIB = re.compile(r"^[a-zA-Z0-9_.-]+:[a-zA-Z0-9_.-]+$")
