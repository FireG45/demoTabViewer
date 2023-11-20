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
        default:
          continue;
      }
    }
    return modificators
}

export default parseEffects;
