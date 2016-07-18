#!/bin/bash

#Gets the SimianArmy repo and sets up for our accounts.

#Should be a local copy so we don't deal with unexpected version differences?
#Should definitely use the kenzanmedia vars
cd /{{ simian_army_location }}
git clone git://github.com/Netflix/SimianArmy.git
