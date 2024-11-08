import socket

choice = input("1. IP to Hostname\n2. Hostname to IP\nChoose an option: ")

try:
    if choice == '1':
        ip = input("Enter IP: ")
        print("Hostname:", socket.gethostbyaddr(ip)[0])
    elif choice == '2':
        hostname = input("Enter Hostname: ")
        print("IP:", socket.gethostbyname(hostname))
    else:
        print("Invalid choice.")
except socket.herror:
    print("Error: Unable to resolve.")
