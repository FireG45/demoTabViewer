import { Annotation, Bend, Vibrato } from 'vexflow'

function parseEffects(effects = []) {
    var modificators = []
    for (let i = 0; i < effects.length; i++) {
      switch (effects[i][0]) {
        case 'b':
          let parts = effects[i].split(" ")
          let text = parts[1].split(":")[1];
          var phrases = []
          phrases.push({type : Bend.UP, text: text})
          if (parts[2]) phrases.push({type : Bend.DOWN, text: ""})
          modificators.push(new Bend(null, null, phrases))
        break;

        case 'v':
          modificators.push(new Vibrato())
        break;

        case 't':
          modificators.push(new Annotation("T"))
        break;

        case 's':
          modificators.push(new Annotation("S"))
        break;

        case 'P':
          modificators.push(new Annotation("P"))
        break;

        case '>':
          modificators.push(new Annotation(">"))
        break;

        case '^':
          modificators.push(new Annotation("^"))
        break;

        case 'h':
          let harmSplit = effects[i].split("|")
          let harmText = harmSplit[1]
          if (harmText[0] === 'H') {
            let fret = parseInt(harmSplit[2])
            if (fret === 5 || fret === 7 || fret === 12
              || fret === 17 || fret === 19 || fret === 24) {
                modificators.push(new Annotation("N.H")) 
              } else {
                modificators.push(new Annotation("A.H")) 
              }
          } else {
            modificators.push(new Annotation(harmText))
          }
        break;

        default:
          continue;
      }
    }
    return modificators
}

export default parseEffects;
