import { Annotation, AnnotationHorizontalJustify, AnnotationVerticalJustify, Bend, GhostNote, Vibrato } from 'vexflow'

function parseEffects(effects = []) {
    var modificators = []
    const annotationShift = -10;
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
          var a = new Annotation("T");
          a.setJustification(AnnotationVerticalJustify)
          modificators.push(a)
        break;

        case 's':
          var a = new Annotation("S");
          modificators.push(a)
        break;

        case 'P':
          var a = new Annotation("P");
          modificators.push(a)
        break;

        case '>':
          var a = new Annotation(">");
          modificators.push(a)
        break;

        case 'h':
          let harmText = effects[i].split("|")[1]
          var a = new Annotation(harmText);
          modificators.push(a)
        break;

        default:
          continue;
      }
    }
    return modificators
}

export default parseEffects;
