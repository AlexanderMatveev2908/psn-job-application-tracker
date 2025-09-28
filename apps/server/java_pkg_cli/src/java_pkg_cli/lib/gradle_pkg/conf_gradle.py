from enum import Enum


class GradleConf(Enum):
    I = "implementation"
    C = "compileOnly"
    R = "runtimeOnly"
    A = "annotationProcessor"
    TI = "testImplementation"
    TC = "testCompileOnly"
    TR = "testRuntimeOnly"
    API = "api"

    @classmethod
    def from_short(cls, short: str) -> str:
        mapping = {
            "i": cls.I,
            "c": cls.C,
            "r": cls.R,
            "a": cls.A,
            "ti": cls.TI,
            "tc": cls.TC,
            "tr": cls.TR,
            "api": cls.API,
        }

        return mapping[short].value
