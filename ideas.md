Kommunikationsmuster: P2P mit Servern

Definition "Stimme": signierte und mit Zeitstempel versehene Daten, die einen ausgefüllten Stimmzettel repräsentieren

Definition "Urne": Datenblock, der alle bekannten Stimmen zusammenfasst

Server und Peers:
* Kennt die Zuordnung ID zu IP der Peers und anderen Server und gibt sie auf Anfrage heraus
* Kennt die Urne und gibt sie auf Anfrage heraus

Peers zusätzlich:
* geben Stimmen ab
* signieren die Urne, sobald sie eine ihnen bisher unbekannte Stimme enthält und senden sie danach an Peers/Server

Wenn beim signieren der Urne die alte Urne nicht Teilmenge der neuen Urne ist, werden die Stimmen der Urnen vereinigt und diese vereinigte Urne wird signiert und ist die neue Urne.
