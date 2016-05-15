# https://github.com/seomoz/dragnet

import sys
import requests
from dragnet import content_extractor

# fetch HTML
# https://github.com/seomoz/dragnet
# https://moz.com/devblog/dragnet-content-extraction-from-diverse-feature-sets/
# http://antonioleiva.com/collapsing-toolbar-layout/
url = sys.argv[1]
r = requests.get(url, timeout=10)

# get main article without comments
content = content_extractor.analyze(r.content)

print content
