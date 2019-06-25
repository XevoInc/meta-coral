SUMMARY = "Edge TPU recipe"
LICENSE = "Apache-2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=19d1f00e43228427ebb9a48f2e175959"

SRC_URI = "\
    git://coral.googlesource.com/edgetpu;protocol=https;branch=${SRCBRANCH}"

SRCBRANCH = "release-chef"
SRCREV = "5df7f62a2d88dc0d9e98c8c794717d77b62daa89"
S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "coral-dev"

RDEPENDS_${PN} = "libusb1 libcxx"

INSANE_SKIP_${PN} = "already-stripped"

do_install() {
    # The Google-provided image uses the throttled version.
    #LIBEDGETPU_SRC="${S}/libedgetpu/libedgetpu_arm64.so"
    LIBEDGETPU_SRC="${S}/libedgetpu/libedgetpu_arm64_throttled.so"

    install -d ${D}/etc/udev/rules.d/
    install ${S}/99-edgetpu-accelerator.rules ${D}/etc/udev/rules.d/

    install -d ${D}/usr/lib
    install ${LIBEDGETPU_SRC} ${D}/usr/lib/libedgetpu.so.1.0

    # Python API.
    #WHEEL=$(ls ${SCRIPT_DIR}/edgetpu-*-py3-none-any.whl 2>/dev/null)
    #if [[ $? == 0 ]]; then
    #    info "Installing Edge TPU Python API..."
    #    sudo python3 -m pip install --no-deps "${WHEEL}"
    #    info "Done."
    #fi
}

