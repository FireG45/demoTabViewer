import { TextBracket } from 'vexflow'

export default function drawLetRing(lrIndexes, notes, context, shift) {
    var letRingTextes = []
    for (let i = 0; i < lrIndexes.length; i++) {
        let start = notes[lrIndexes[i][0]]
        let stop = notes[lrIndexes[i][lrIndexes[i].length - 1]]
        if (lrIndexes[i].length > 1) {
            let pmText = new TextBracket({start : start, stop: stop, text : "let ring", position: 1})
            pmText.fontSize = "10"
            letRingTextes.push(pmText)
        } else {
            context.setFont("Arial", 10);
            context.fillText("let ring", shift + start.getX() + 8, 25);
        }
    }
  
    for (let i = 0; i < letRingTextes.length; i++) {
        const element = letRingTextes[i];
        element.setContext(context).draw();
    }
}