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
          let harmText = effects[i].split("|")[1]
          modificators.push(new Annotation(harmText))
        break;

        default:
          continue;
      }
    }
    return modificators
}

export default parseEffects;
