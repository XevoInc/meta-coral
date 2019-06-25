SUMMARY = "Edge TPU vision recipe"
LICENSE = "Apache-2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE-2.0.txt;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "\
    git://coral.googlesource.com/edgetpuvision;protocol=https;branch=${SRCBRANCH} \
    https://www.apache.org/licenses/LICENSE-2.0.txt;md5sum=3b83ef96387f14655fc854ddc3c6bd57"

SRCBRANCH = "release-chef"
SRCREV = "437ef5b065892eca461aee3db117abefc33d28aa"
S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "coral-dev"

#DEPENDS = "edgetpu"
RDEPENDS_${PN} = "edgetpu"

#INSANE_SKIP_${PN} = "already-stripped"

inherit setuptools3

do_install() {
	python3 setup.py install
}

