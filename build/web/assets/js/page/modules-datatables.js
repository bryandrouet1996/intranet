"use strict";

$("[data-checkboxes]").each(function() {
  var me = $(this),
    group = me.data('checkboxes'),
    role = me.data('checkbox-role');
  me.change(function() {
    var all = $('[data-checkboxes="' + group + '"]:not([data-checkbox-role="dad"])'),
      checked = $('[data-checkboxes="' + group + '"]:not([data-checkbox-role="dad"]):checked'),
      dad = $('[data-checkboxes="' + group + '"][data-checkbox-role="dad"]'),
      total = all.length,
      checked_length = checked.length;
    if(role == 'dad') {
      if(me.is(':checked')) {
        all.prop('checked', true);
      }else{
        all.prop('checked', false);
      }
    }else{
      if(checked_length >= total) {
        dad.prop('checked', true);
      }else{
        dad.prop('checked', false);
      }
    }
  });
});


$("#table-1").dataTable({
  "ordering": true,
  "order": [[ 0, 'desc' ], [ 1, 'desc' ]],
  "columnDefs": [
    { "sortable": false, "targets": [0,4] }
  ],"pageLength": 25,
  language: {
        "decimal":        "",
    "emptyTable":     "No hay datos",
    "info":           "Mostrando _START_ a _END_ de _TOTAL_ registros",
    "infoEmpty":      "Mostrando 0 a 0 de 0 registros",
    "infoFiltered":   "(Filtro de _MAX_ total registros)",
    "infoPostFix":    "",
    "thousands":      ",",
    "lengthMenu":     "Mostrar _MENU_ registros",
    "loadingRecords": "Cargando...",
    "processing":     "Procesando...",
    "search":         "Buscar:",
    "zeroRecords":    "No se encontraron coincidencias",
    "paginate": {
        "first":      "Primero",
        "last":       "Ultimo",
        "next":       "Próximo",
        "previous":   "Anterior"
    }
  }
});

$("#table-2").dataTable({
 "ordering": true,
 "order": [[ 0, 'desc' ], [ 1, 'desc' ]],
 "columnDefs": [
    { "sortable": false, "targets": [2,3] }
  ],"pageLength": 25,
  language: {
        "decimal":        "",
    "emptyTable":     "No hay datos",
    "info":           "Mostrando _START_ a _END_ de _TOTAL_ registros",
    "infoEmpty":      "Mostrando 0 a 0 de 0 registros",
    "infoFiltered":   "(Filtro de _MAX_ total registros)",
    "infoPostFix":    "",
    "thousands":      ",",
    "lengthMenu":     "Mostrar _MENU_ registros",
    "loadingRecords": "Cargando...",
    "processing":     "Procesando...",
    "search":         "Buscar:",
    "zeroRecords":    "No se encontraron coincidencias",
    "paginate": {
        "first":      "Primero",
        "last":       "Ultimo",
        "next":       "Próximo",
        "previous":   "Anterior"
    }
  }
});

$("#table-3").dataTable({
  "ordering": true,
  "order": [[ 0, 'desc' ], [ 1, 'desc' ]],
  "columnDefs": [
    { "sortable": false, "targets": [2,3]}
  ],"pageLength": 25,
  language: {
        "decimal":        "",
    "emptyTable":     "No hay datos",
    "info":           "Mostrando _START_ a _END_ de _TOTAL_ registros",
    "infoEmpty":      "Mostrando 0 a 0 de 0 registros",
    "infoFiltered":   "(Filtro de _MAX_ total registros)",
    "infoPostFix":    "",
    "thousands":      ",",
    "lengthMenu":     "Mostrar _MENU_ registros",
    "loadingRecords": "Cargando...",
    "processing":     "Procesando...",
    "search":         "Buscar:",
    "zeroRecords":    "No se encontraron coincidencias",
    "paginate": {
        "first":      "Primero",
        "last":       "Ultimo",
        "next":       "Próximo",
        "previous":   "Anterior"
    }
  }
});

$("#table-4").dataTable({
  "ordering": true,
  "order": [[ 0, 'desc' ], [ 1, 'desc' ]],
  "columnDefs": [
    { "sortable": false, "targets": [2,3]}
  ],"pageLength": 25,
  language: {
        "decimal":        "",
    "emptyTable":     "No hay datos",
    "info":           "Mostrando _START_ a _END_ de _TOTAL_ registros",
    "infoEmpty":      "Mostrando 0 a 0 de 0 registros",
    "infoFiltered":   "(Filtro de _MAX_ total registros)",
    "infoPostFix":    "",
    "thousands":      ",",
    "lengthMenu":     "Mostrar _MENU_ registros",
    "loadingRecords": "Cargando...",
    "processing":     "Procesando...",
    "search":         "Buscar:",
    "zeroRecords":    "No se encontraron coincidencias",
    "paginate": {
        "first":      "Primero",
        "last":       "Ultimo",
        "next":       "Próximo",
        "previous":   "Anterior"
    }
  }
});

$("#table-5").dataTable({
  "ordering": true,
  "order": [[ 0, 'desc' ], [ 1, 'desc' ]],
  "columnDefs": [
    { "sortable": false, "targets": [2,3] }
  ],"pageLength": 25,
  language: {
        "decimal":        "",
    "emptyTable":     "No hay datos",
    "info":           "Mostrando _START_ a _END_ de _TOTAL_ registros",
    "infoEmpty":      "Mostrando 0 a 0 de 0 registros",
    "infoFiltered":   "(Filtro de _MAX_ total registros)",
    "infoPostFix":    "",
    "thousands":      ",",
    "lengthMenu":     "Mostrar _MENU_ registros",
    "loadingRecords": "Cargando...",
    "processing":     "Procesando...",
    "search":         "Buscar:",
    "zeroRecords":    "No se encontraron coincidencias",
    "paginate": {
        "first":      "Primero",
        "last":       "Ultimo",
        "next":       "Próximo",
        "previous":   "Anterior"
    }
  }
});

$("#table-rank").dataTable({
  "ordering": false,
  "order": [[ 0, 'desc' ], [ 1, 'desc' ]],
  "columnDefs": [
    { "sortable": false, "targets": [2,3] }
  ],"pageLength": 50,
  language: {
        "decimal":        "",
    "emptyTable":     "No hay datos",
    "info":           "Mostrando _START_ a _END_ de _TOTAL_ registros",
    "infoEmpty":      "Mostrando 0 a 0 de 0 registros",
    "infoFiltered":   "(Filtro de _MAX_ total registros)",
    "infoPostFix":    "",
    "thousands":      ",",
    "lengthMenu":     "Mostrar _MENU_ registros",
    "loadingRecords": "Cargando...",
    "processing":     "Procesando...",
    "search":         "Buscar:",
    "zeroRecords":    "No se encontraron coincidencias",
    "paginate": {
        "first":      "Primero",
        "last":       "Ultimo",
        "next":       "Próximo",
        "previous":   "Anterior"
    }
  }
});

$("#table-resumen").dataTable({
 "ordering": true,
 "order": [[ 0, 'desc' ], [ 1, 'desc' ]],
 "columnDefs": [
    { "sortable": false, "targets": [2,3] }
  ],"pageLength": 25,
  dom: 'Bfrtip',
  buttons: [
      {
        extend: 'copy',
        text: 'Copiar <i class="fas fa-copy"></i>'
      },
      {
        extend: 'excel',
        text: 'Exportar a Excel <i class="fas fa-file-excel"></i>'
      },
      {
        extend: 'pdf',
        text: 'Exportar a PDF <i class="far fa-file-pdf"></i>'
      }
      ,{
        extend: 'print',
        text: 'Imprimir <i class="fas fa-print"></i>'
      }
    ],
  language: {
        "decimal":        "",
    "emptyTable":     "No hay datos",
    "info":           "Mostrando _START_ a _END_ de _TOTAL_ registros",
    "infoEmpty":      "Mostrando 0 a 0 de 0 registros",
    "infoFiltered":   "(Filtro de _MAX_ total registros)",
    "infoPostFix":    "",
    "thousands":      ",",
    "lengthMenu":     "Mostrar _MENU_ registros",
    "loadingRecords": "Cargando...",
    "processing":     "Procesando...",
    "search":         "Buscar:",
    "zeroRecords":    "No se encontraron coincidencias",
    "paginate": {
        "first":      "Primero",
        "last":       "Ultimo",
        "next":       "Próximo",
        "previous":   "Anterior"
    }
  }
});

$("#table-resumen1").dataTable({
 "ordering": true,
 "order": [[ 0, 'desc' ], [ 1, 'desc' ]],
 "columnDefs": [
    { "sortable": false, "targets": [2,3] }
  ],"pageLength": 25,
  dom: 'Bfrtip',
  buttons: [
      {
        extend: 'copy',
        text: 'Copiar <i class="fas fa-copy"></i>'
      },
      {
        extend: 'excel',
        text: 'Exportar a Excel <i class="fas fa-file-excel"></i>'
      },
      {
        extend: 'pdf',
        text: 'Exportar a PDF <i class="far fa-file-pdf"></i>'
      }
      ,{
        extend: 'print',
        text: 'Imprimir <i class="fas fa-print"></i>'
      }
    ],
  language: {
        "decimal":        "",
    "emptyTable":     "No hay datos",
    "info":           "Mostrando _START_ a _END_ de _TOTAL_ registros",
    "infoEmpty":      "Mostrando 0 a 0 de 0 registros",
    "infoFiltered":   "(Filtro de _MAX_ total registros)",
    "infoPostFix":    "",
    "thousands":      ",",
    "lengthMenu":     "Mostrar _MENU_ registros",
    "loadingRecords": "Cargando...",
    "processing":     "Procesando...",
    "search":         "Buscar:",
    "zeroRecords":    "No se encontraron coincidencias",
    "paginate": {
        "first":      "Primero",
        "last":       "Ultimo",
        "next":       "Próximo",
        "previous":   "Anterior"
    }
  }
});

$("#table-resumen2").dataTable({
 "ordering": true,
 "order": [[ 0, 'desc' ], [ 1, 'desc' ]],
 "columnDefs": [
    { "sortable": false, "targets": [2,3] }
  ],"pageLength": 25,
  dom: 'Bfrtip',
  buttons: [
      {
        extend: 'copy',
        text: 'Copiar <i class="fas fa-copy"></i>'
      },
      {
        extend: 'excel',
        text: 'Exportar a Excel <i class="fas fa-file-excel"></i>'
      },
      {
        extend: 'pdf',
        text: 'Exportar a PDF <i class="far fa-file-pdf"></i>'
      }
      ,{
        extend: 'print',
        text: 'Imprimir <i class="fas fa-print"></i>'
      }
    ],
  language: {
        "decimal":        "",
    "emptyTable":     "No hay datos",
    "info":           "Mostrando _START_ a _END_ de _TOTAL_ registros",
    "infoEmpty":      "Mostrando 0 a 0 de 0 registros",
    "infoFiltered":   "(Filtro de _MAX_ total registros)",
    "infoPostFix":    "",
    "thousands":      ",",
    "lengthMenu":     "Mostrar _MENU_ registros",
    "loadingRecords": "Cargando...",
    "processing":     "Procesando...",
    "search":         "Buscar:",
    "zeroRecords":    "No se encontraron coincidencias",
    "paginate": {
        "first":      "Primero",
        "last":       "Ultimo",
        "next":       "Próximo",
        "previous":   "Anterior"
    }
  }
});

$("#table-resumen3").dataTable({
 "ordering": true,
 "order": [[ 0, 'desc' ], [ 1, 'desc' ]],
 "columnDefs": [
    { "sortable": false, "targets": [2,3] }
  ],"pageLength": 25,
  dom: 'Bfrtip',
  buttons: [
      {
        extend: 'copy',
        text: 'Copiar <i class="fas fa-copy"></i>'
      },
      {
        extend: 'excel',
        text: 'Exportar a Excel <i class="fas fa-file-excel"></i>'
      },
      {
        extend: 'pdf',
        text: 'Exportar a PDF <i class="far fa-file-pdf"></i>'
      }
      ,{
        extend: 'print',
        text: 'Imprimir <i class="fas fa-print"></i>'
      }
    ],
  language: {
        "decimal":        "",
    "emptyTable":     "No hay datos",
    "info":           "Mostrando _START_ a _END_ de _TOTAL_ registros",
    "infoEmpty":      "Mostrando 0 a 0 de 0 registros",
    "infoFiltered":   "(Filtro de _MAX_ total registros)",
    "infoPostFix":    "",
    "thousands":      ",",
    "lengthMenu":     "Mostrar _MENU_ registros",
    "loadingRecords": "Cargando...",
    "processing":     "Procesando...",
    "search":         "Buscar:",
    "zeroRecords":    "No se encontraron coincidencias",
    "paginate": {
        "first":      "Primero",
        "last":       "Ultimo",
        "next":       "Próximo",
        "previous":   "Anterior"
    }
  }
});




