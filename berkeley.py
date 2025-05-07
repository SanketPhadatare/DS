from dateutil import parser
import threading
import datetime
import socket
import time

client_data = {}

def startReceivingClockTime(connector, address):
    while True:
        try:
            clock_time_str = connector.recv(1024).decode()
            clock_time = parser.parse(clock_time_str)
            clock_time_diff = datetime.datetime.now() - clock_time
            client_data[address] = {
                "clock_time": clock_time,
                "time_difference": clock_time_diff,
                "connector": connector
            }
            print("Client Data updated with: " + str(address) + "\n")
            time.sleep(5)
        except:
            continue

def startConnecting(master_server):
    while True:
        master_slave_connector, addr = master_server.accept()
        slave_address = str(addr[0]) + ":" + str(addr[1])
        print(slave_address + " connected successfully")
        thread = threading.Thread(target=startReceivingClockTime, args=(master_slave_connector, slave_address,))
        thread.start()

def getAverageClockDiff():
    current_data = client_data.copy()
    if not current_data:
        return datetime.timedelta()
    diffs = [client['time_difference'] for client in current_data.values()]
    return sum(diffs, datetime.timedelta(0)) / len(diffs)

def synchronizeAllClocks():
    while True:
        print("Synchronization cycle started. Clients: " + str(len(client_data)))
        if client_data:
            avg_diff = getAverageClockDiff()
            for client_addr, client in client_data.items():
                try:
                    sync_time = datetime.datetime.now() + avg_diff
                    client['connector'].send(str(sync_time).encode())
                except Exception as e:
                    print(f"Error sending to {client_addr}: {e}")
        else:
            print("No clients to sync.")
        print("\n")
        time.sleep(5)

def initiateClockServer(port=8080):
    master_server = socket.socket()
    master_server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    master_server.bind(('', port))
    master_server.listen(10)

    print("Clock server started. Waiting for clients...\n")
    threading.Thread(target=startConnecting, args=(master_server,)).start()
    threading.Thread(target=synchronizeAllClocks).start()

if __name__ == '__main__':
    initiateClockServer(port=8080)
    
    
    
    
    from dateutil import parser
import threading
import datetime
import socket
import time

def startSendingTime(slave_client):
    while True:
        try:
            slave_client.send(str(datetime.datetime.now()).encode())
            print("Time sent to server\n")
            time.sleep(5)
        except:
            continue

def startReceivingTime(slave_client):
    while True:
        try:
            sync_time = parser.parse(slave_client.recv(1024).decode())
            print("Synchronized time at client: " + str(sync_time) + "\n")
        except:
            continue

def initiateSlaveClient(port=8080):
    slave_client = socket.socket()
    slave_client.connect(('127.0.0.1', port))

    print("Connected to server, starting sync...\n")

    threading.Thread(target=startSendingTime, args=(slave_client,)).start()
    threading.Thread(target=startReceivingTime, args=(slave_client,)).start()

if __name__ == '__main__':
    initiateSlaveClient(port=8080)
    
    
    
    
    
 #python3
 #gedit &
 #python3 berkeley.py


