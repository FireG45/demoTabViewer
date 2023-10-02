import React, { useRef, useEffect } from 'react'
import VexFlow from 'vexflow'
const { Renderer, TabStave, TabNote, Formatter} = VexFlow.Flow


export default function Score({
  staves = [],
}) {
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
    
    // Configure the rendering context.
    renderer.resize(500, 300);
    const context = renderer.getContext();

    // Create a tab stave of width 400 at position 10, 40 on the canvas.
    const stave = new TabStave(10, 40, 400);
    stave.addClef("tab").setContext(context).draw();

    const notes = [
        // A single note
        new TabNote({
            positions: [{ str: 3, fret: 7 }],
            duration: "q",
        }),
    ];

    Formatter.FormatAndDraw(context, stave, notes);

  })

  return <div ref={container} />
}