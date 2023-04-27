# ARCAD Evolution bundles
This project holds several Eclipse bundles used by ARCAD products.

## Development
Recommended tools for maintaining this project:
- [Java 17](https://adoptium.net/temurin/releases/?version=17)
- [Eclipse IDE](https://www.eclipse.org/downloads/)
- Eclipse m2e integration plugin

[![Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client](https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.svg)](https://marketplace.eclipse.org/marketplace-client-intro?mpc_install=5321178 "Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client")

## Build
### Requirements
[Java 17](https://adoptium.net/temurin/releases/?version=17) and [Apache Maven](https://maven.apache.org/download.cgi).

### Steps
1. Clone this repository
2. Run `mvn package`
3. Find the built P2 repository under `releng/aev/update/target/repository`

## P2 locations
- `main` branch prerelease: https://github.com/ARCAD-Software/arcad-evolution/releases/download/latest
