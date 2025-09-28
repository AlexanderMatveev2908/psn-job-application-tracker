from tomlkit import table


class CtxCatalog:
    def __init__(self, doc) -> None:
        self.doc = doc
        self.versions = doc.get("versions") or table()
        self.libs = doc.get("libraries") or table()
        self.plugins = doc.get("plugins") or table()

        doc["versions"] = self.versions
        doc["libraries"] = self.libs
        doc["plugins"] = self.plugins
