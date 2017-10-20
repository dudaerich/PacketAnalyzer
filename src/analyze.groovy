import org.apache.activemq.packet.analyzer.Participant
import org.apache.activemq.packet.analyzer.events.Event

def participants = []

for (String arg : args) {
    def s = arg.split("=")
    Participant participant = new Participant(s[0])
    InputStream is = new FileInputStream(s[1])
    participant.readLog(is)
    is.close()
    participants.add(participant)
}

def events = []

for (Participant participant : participants) {
    events.addAll(participant.events)
}

def filters = [
        { it.channelIDInt == 2 },
        { it.channelIDInt >= 10 }
]

events = events.findAll {
    for (f in filters) {
        if (f(it)) return true
    }
    return false
}

events.sort { it.time }

println """<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Packet Analyzer output</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
    <style>
      td {
        font-size: 9px;
        font-family: monospace;
        margin: 0;
      }
    </style>
  </head>
  <body>
  <table class="table table-condensed">
"""

for (Event e : events) {
    println e.toHtml()
}

println """
  </table>
  </body>
</html>
"""
