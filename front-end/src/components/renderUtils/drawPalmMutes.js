import { TextBracket } from 'vexflow'

export default function drawPalmMutes(pmIndexes, notes, context, shift) {
    var palmMutedTextes = []
    for (let i = 0; i < pmIndexes.length; i++) {
        let start = notes[pmIndexes[i][0]]
        let stop = notes[pmIndexes[i][pmIndexes[i].length - 1]]
        if (pmIndexes[i].length > 1) {
            let pmText = new TextBracket({start : start, stop: stop, text : "P.M", position: 1})
            pmText.fontSize = "10"
            palmMutedTextes.push(pmText)
        } else {
            context.setFont("Arial", 10);
            context.fillText("P.M", shift + start.getX() + 8, 25);
        }
    }
  
    for (let i = 0; i < palmMutedTextes.length; i++) {
        const element = palmMutedTextes[i];
        element.setContext(context).draw();
    }
}