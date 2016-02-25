#!/usr/bin/env python

import sys
import re
import base64

try:
    import requests
except ImportError, e:
    print "Missing requests module.  Install with: sudo pip install requests"
    print "If you don't have pip, do this first: sudo easy_install pip"
    exit(2)


'''
@author: James Pancoast (jpancoast@kenzan.com)
@description: A simple script to set/get/delete consul/etcd kv values. At least in theory it might work for etcd. Haven't tested it on etcd.
'''


def main(argv):
    (type, address, action, key, value) = parse_cli()

    if value is not None:
        print "Value to set: " + str(value)

    base_url = ''
    address = re.sub('http://', '', address)
    if type == 'etcd':
        base_url = address + '/v2/keys/'
    elif type == 'consul':
        base_url = address + '/v1/kv/'

    base_url = re.sub('\/+', '/', base_url)

    base_url = 'http://' + base_url
    base_url = re.sub('\/+$', '', base_url)

    if action.lower() == 'set':
        set_key_value(base_url, key, value)
    elif action.lower() == 'get':
        value = parse_value(get_key_value(base_url, key), type)

        if value is not None:
            print value
        else:
            print ''

    elif action.lower() == 'delete':
        delete_key_value(base_url, key)


def parse_value(r, type):
    ret_val = None

    if type == 'etcd':
        try:
            response = r.json()
            if 'value' in response:
                ret_val = value
        except ValueError, e:
            pass
    elif type == 'consul':
        try:
            response = r.json()[0]
            if 'Value' in response:
                ret_val = base64.b64decode(response['Value'])
        except ValueError, e:
            pass

    return ret_val


def get_key_value(base_url, key):
    key = re.sub('^/', '', key)
    full_url = base_url + '/' + key

    return requests.get(full_url)


def set_key_value(base_url, key, value):
    key = re.sub('^/+', '', key)
    full_url = base_url + '/' + key

    r = requests.put(full_url, data=value)


def delete_key_value(base_url, key):
    key = re.sub('^/+', '', key)
    full_url = base_url + '/' + key

    r = requests.delete(full_url)


def parse_cli():
    if len(sys.argv) > 3 and sys.argv[3] == 'set':
        # Need the value, so one more argument (5 total)
        if len(sys.argv) == 6:
            return (sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5])
    else:
        # Only need 4 arguments
        if len(sys.argv) == 5:
            return (sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], None)

    usage()


def usage():
    print "Usage:"
    print "\t" + sys.argv[0] + ' <etcd|consul> <address> <set|get|delete> <key> (<value>)'
    sys.exit(1)

if __name__ == "__main__":
    main(sys.argv)
