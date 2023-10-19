import React, { useRef, useEffect } from 'react'
import VexFlow, { Beam, Bend, TextBracket, Vibrato } from 'vexflow'
const { Renderer, TabStave, TabNote, Formatter, StaveNote } = VexFlow.Flow

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
      default:
      break;
    }
  }
  return modificators
}

export default function Stave({ measure = null, stringCount = 6, tempo = 0, timeSignature = "", tuning = "" }) {
  const container = useRef()
  const rendererRef = useRef()

  useEffect(() => {
    if (rendererRef.current == null) {
      rendererRef.current = new Renderer(
        container.current,
        Renderer.Backends.SVG
      )
    }
    const renderer = rendererRef.current
    
    renderer.resize(500, 160 + (stringCount % 6 * 15));
    const context = renderer.getContext();

    let shift = tuning ? 20 : 0
    const stave = new TabStave(shift, 0, 448 - shift);
    stave.options.num_lines = stringCount;
    stave.options.line_config = new Array(stringCount).fill({visible : true});
    
    if (tempo) {
      const bpm = tempo;
      
      context.setFont("Arial", 20);
      context.fillText("♩" , 5, 25);
      context.setFont("Arial", 12);
      context.fillText(bpm, 0, 40);
    }

    if (tuning) {
      context.setFont("Arial", 10);
      for (let i = 1; i < tuning.length; i++) {
        const element = tuning[i - 1];
        context.fillText(element , 0, i * 13 + 43);
      }
    }

    if (timeSignature) {
      stave.addTimeSignature(timeSignature);
    }

    stave.setContext(context).draw()
    
    let notes = [];
    let beamNotes = [];
    let palmMutedNotes = [];
    for (let i = 0; i < measure.length; i++) {
      let beat = measure[i].noteDTOS;
      let duration = measure[i].duration;
      let effects = measure[i].effects;
      let palmMute = measure[i].palmMute;
      var pos = []

      for (let j = 0; j < beat.length; j++) {
        pos.push({ str: beat[j].string, fret: beat[j].fret })
      }
      var note
      if (pos.length > 0) {
        note = new TabNote({positions: pos, duration: duration})

        let parsedEffects = parseEffects(effects);
        
        for (let i = 0; i < parsedEffects.length; i++) {
          const element = parsedEffects[i];
          note.addModifier(element);
        }

        notes.push(note)
        beamNotes.push(note)
      } else {
        notes.push(new StaveNote({ keys: ["b/4"], duration: duration + "r"}))
      } 
      if ((palmMute && (i === 0 || i === measure.length - 1)) || (i > 0 && i < measure.length - 1 && measure[i - 1].palmMute === !palmMute)) {
        palmMutedNotes.push(notes[i])
      }
    }

    Formatter.FormatAndDraw(context, stave, notes);

    for (let i = 0; i < palmMutedNotes.length - 1; i++) {
      let stop = i + 1 < palmMutedNotes.length ? palmMutedNotes[i + 1] : palmMutedNotes[i]
      let pmText = new TextBracket({start : palmMutedNotes[i], stop: stop, text : "P.M", position: 1})
      pmText.fontSize = "10"
      pmText.setContext(context).draw();
    }

    var beams = Beam.generateBeams(notes, {
      stem_direction: -1, flat_beams :true, flat_beam_offset : -10
    });

    beams.forEach(function(beam) {
      beam.setContext(context).draw();
    });
  })
  return <div ref={container} />
}