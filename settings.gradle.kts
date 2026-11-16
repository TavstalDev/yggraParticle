rootProject.name = "yggraParticle"

include(
    "api",
    "impl_v1_8_8",
    "impl_v1_9_4",
    "bootstrap",
    "plugin-test"
)

project(":impl_v1_8_8").projectDir = file("implementation/impl_v1_8_8")
project(":impl_v1_9_4").projectDir = file("implementation/impl_v1_9_4")