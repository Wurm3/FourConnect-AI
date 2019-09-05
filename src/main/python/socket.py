import socket
import sys

p socket.new
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_address('localhost',1025)
sock.connect(server_address)

try:
    message = "Memememem"
    socker.sendall(message)
finally:
    print('closing socket')
    sock.close()