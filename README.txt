Art-Net by Artistic Licence allows for broadcasting DMX data via IP/UDP. This library is implementing the basic protocol for Java applications.

Currently supported core features are:

Device/node discovery & automatic updating of node configurations
Java typed descriptors of nodes & node properties
Abstracted DmxUniverse allowing direct pixel/channel access without having to deal with packets
JAXB configuration support for storing universe/node descriptions as XML
Listener support for various events
Sending of DMX512 data via UDP broadcast or unicast
This project is currently still in pre-alpha stage, so currently only source access via git. Be also aware that large parts of the codebase are still undergoing major changes.

artnet4j-elios is a fork of original artnet4j lib developped by Karsten Schmidt and hosted here : http://code.google.com/p/artnet4j/