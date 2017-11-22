log = open("log.txt", "r")

for line in log:
    part = line.split("_")
    sender = part[1]
    receiver = part[3]
    message = part[5]
    
    if part[0] == "receiving":
    	new_buffer = part[7]
    	old_buffer = part[9]
    	...
    	continue
    
    if part[0] == "delivering":
    	...
    	continue
