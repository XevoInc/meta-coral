SUMMARY = "Edge TPU recipe"
LICENSE = "Apache-2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=19d1f00e43228427ebb9a48f2e175959"

SRC_URI = "\
    git://coral.googlesource.com/edgetpu;protocol=https;branch=${SRCBRANCH} \
    file://edgetpu-1.9.2-py3-none-any.whl;md5sum=55652fd9bc73729fef74aa13e46d8d01O"

SRCBRANCH = "release-chef"
SRCREV = "5df7f62a2d88dc0d9e98c8c794717d77b62daa89"
S = "${WORKDIR}/git"
PACKAGES = "${PN} ${PN}-dev ${PN}-dbg python3-${PN}"

COMPATIBLE_MACHINE = "coral-dev"

RDEPENDS_${PN} = "libusb1 libcxx"
RDEPENDS_python3-${PN} = "${PN} python3-numpy python3-pillow"
DEPENDS = "python3 python3-pip-native"

# Since this package includes shared objects, the packager wants to prepend it
# with "lib".
DEBIANNAME_python3-${PN} = "python3-${PN}"

INSANE_SKIP_${PN} = "already-stripped dev-so"

inherit python3native

FILES_python3-${PN} = "${PYTHON_SITEPACKAGES_DIR}"

do_install() {
    set -x
    # The Google-provided image uses the throttled version.
    #LIBEDGETPU_SRC="${S}/libedgetpu/libedgetpu_arm64.so"
    LIBEDGETPU_SRC="${S}/libedgetpu/libedgetpu_arm64_throttled.so"

    install -d ${D}/etc/udev/rules.d/
    install ${S}/99-edgetpu-accelerator.rules ${D}/etc/udev/rules.d/

    install -d ${D}/usr/lib
    install ${LIBEDGETPU_SRC} ${D}/usr/lib/libedgetpu.so.1.0

    # Python API.
    WHEEL=$(ls ${WORKDIR}/edgetpu-*-py3-none-any.whl 2>/dev/null)
    ${PYTHON} -m pip install --no-deps --no-cache-dir -I --disable-pip-version-check --prefix=${D}/usr -v "${WHEEL}"
    # Remove non aarch64 wrappers. Using readelf or file would be more flexible.
    find ${D}/${PYTHON_SITEPACKAGES_DIR} -name "*-x86_64-linux-gnu.so*" |xargs rm
    find ${D}/${PYTHON_SITEPACKAGES_DIR} -name "*-arm-linux-gnueabihf.so*" |xargs rm
}

