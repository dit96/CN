def calculate_subnet(cidr):
    subnet_bits = cidr - 24
    host_bits = 8 - subnet_bits
    total_networks = 2 ** subnet_bits
    total_hosts = (2 ** host_bits) - 2  # Subtract 2 for network and broadcast addresses
    print("Total networks:", total_networks)
    print("Hosts per network:", total_hosts)

def get_ip_class(ip):
    octets = ip.split(".")
    if len(octets) != 4 or not all(octet.isdigit() and 0 <= int(octet) <= 255 for octet in octets):
        return "Invalid IP"
    
    first_octet = int(octets[0])
    if 1 <= first_octet <= 126:
        return "Class A"
    elif 128 <= first_octet <= 191:
        return "Class B"
    elif 192 <= first_octet <= 223:
        return "Class C"
    elif 224 <= first_octet <= 239:
        return "Class D"
    elif 240 <= first_octet <= 254:
        return "Class E"
    elif first_octet == 127:
        return "Reserved for loopback"
    elif first_octet == 255:
        return "Reserved for broadcasting"
    else:
        return "Invalid IP"

# Main program
ip = input("Enter IP (e.g., 192.168.10.0): ")
print("IP Class:", get_ip_class(ip))

cidr = input("Enter CIDR bits (24-32): ")
if cidr.isdigit() and 24 <= int(cidr) <= 32:
    calculate_subnet(int(cidr))
else:
    print("CIDR should be a number between 24 and 32.")
