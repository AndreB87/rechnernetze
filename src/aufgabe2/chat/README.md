# Chat Application Spezifikation

Im folgenden wird das Protokoll für die Kommunikation zwischen einem Chat Client
und einem Chat Server spezifiziert.

## Chat Client Befehle

Hier werden einmal alle Befehle aufgelistet die ein Chat Client über eine TCP-Verbindung
zu einem Chat Server schicken kann und was genau in Fehlerfällen passiert.

### HELLO Befehl

Baut eine neue Verbindung zwischen einem Chat Server und dem Client auf.

```
> HELLO <host name> <port number>
< HELLO from <chat server name>
```

### GOODBYE Befehl

Baut eine neue Verbindung zwischen einem Chat Server und dem Client auf.

```
> GOODBYE
< GOODBYE until next time
```

### LIST ROOMS Befehl

Listet alle offenen Chat-Räume im Server auf, denen der Client beitreten kann.

```
> LIST ROOMS
< AVAILABLE ROOMS: <room1 id> <room2 id> ...
```

### JOIN ROOM Befehl

Der Client tritt einem Raum mit einem entsprechenden unique screen name bei.

```
> JOIN ROOM <room id> <screen name>
< JOINED ROOM
```

Falls der Raum nicht vorhanden ist:

```
> JOIN ROOM <wrong room id> <screen name>
< NO SUCH ROOM
```

Falls der Screen Name bereits vergeben ist:

```
> JOIN ROOM <wrong room id> <screen name>
< NAME IN USE
```

### LEAVE ROOM Befehl

Der Client verlässt einem Raum.

```
> LEAVE ROOM
< LEFT ROOM
```

Falls der Client noch keinem Raum beigetreten ist:

```
> LEAVE ROOM
< NO ROOM TO LEAVE
```

### MESSAGE Befehl

Eine Nachricht wird an den Chat gesendet, in dem sich der Client zurzeit befindet.
Der Raum schickt sofort eine erhaltene Nachricht an alle Clients, 
die sich zurzeit im Raum befinden. Die erhaltene Nachricht wird vom Client in einer
Ausgabe angezeigt.

```
> MESSAGE <nachricht>
< MESSAGE <nachricht>
```

Falls der Client noch keinem Raum beigetreten ist:

```
> MESSAGE <nachricht>
< NO ROOM TO MESSAGE
```

