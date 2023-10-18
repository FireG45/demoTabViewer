import React, { useRef, useEffect } from 'react'
import VexFlow from 'vexflow'
const { Renderer, TabStave, TabNote, Formatter, StaveNote} = VexFlow.Flow

export default function Stave({ measure = null, stringCount = 6 }) {
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
    
    renderer.resize(500, 130);
    const context = renderer.getContext();

    const stave = new TabStave(0, 0, 350);
    stave.options.num_lines = stringCount;
    stave.options.line_config = new Array(stringCount).fill({visible : true});
    stave.setContext(context).draw();
    let notes = [];

    for (let i = 0; i < measure.length; i++) {
      let beat = measure[i].noteDTOS
      var pos = []

      for (let j = 0; j < beat.length; j++) {
        pos.push({ str: parseInt(beat[j].string), fret: parseInt(beat[j].fret) })
      }
      var note
      if (pos.length > 0) {
        note = new TabNote({positions: pos, duration: "q"})
        notes.push(note)
      } else {
        notes.push(new StaveNote({ keys: ["b/4"], duration: "qr" }))
      } 
    }

    if (notes.length !== 0) {
      Formatter.FormatAndDraw(context, stave, notes);
    }
  })
  return <div ref={container} />
}