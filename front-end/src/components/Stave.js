import React, { useRef, useEffect } from 'react'
import VexFlow, { Beam, TabSlide, TextBracket } from 'vexflow'
import parseEffects from './utils/parseEffects'
const { Renderer, TabStave, TabNote, Formatter, StaveNote } = VexFlow.Flow

export default function Stave({ measure = null, stringCount = 6, tempo = 0, timeSignature = "", tuning = "", staveId = 0, pmIndexes = null , slidesAndTies = null}) {
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
      const x = measure[0] && measure[0].effects[0] === 't' ? -12 : 0;
      const y = 0;
      context.setFont("Arial", 20);
      context.fillText("♩" , y + 5, x + 25);
      context.setFont("Arial", 12);
      context.fillText(bpm, y, x + 40);
    }

    if (staveId) {
      const id = staveId;
      
      context.setFont("Arial", 8);
      context.fillText(id, shift, 50);
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
    var palmMutedTextes = []
    for (let i = 0; i < measure.length; i++) {
      let beat = measure[i].noteDTOS;
      let duration = measure[i].duration;
      let effects = measure[i].effects;
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
    }

    if (notes && notes.length > 0) Formatter.FormatAndDraw(context, stave, notes);

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

    for (let i = 0; i < slidesAndTies.length; i++) {
      let splitted = slidesAndTies[i].split("|")
      let from = notes[parseInt(splitted[0])]
      let to = notes[parseInt(splitted[1])]
      let indices = []
      if (!from.getPositions || !to.getPositions) continue;
      for (let i = 0; i < from.getPositions().length; i++) indices.push(i)
      var tie = new TabSlide({
        first_note: from,
        last_note: to,
        first_indices: indices,
        last_indices: indices,
      }, from.getPositions()[0].fret < to.getPositions()[0].fret ? TabSlide.SLIDE_UP : TabSlide.SLIDE_DOWN);

      tie.setContext(context);
      tie.draw();
    }


    for (let i = 0; i < palmMutedTextes.length; i++) {
      const element = palmMutedTextes[i];
      element.setContext(context).draw();
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