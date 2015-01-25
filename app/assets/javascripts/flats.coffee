jQuery ($) ->
    $table = $('.container table')
    flatListUrl = $table.data('list')

    loadFlatTable = ->
        $.get flatListUrl, (flats) ->
            $.each flats, (index, flatId) ->
                row = $('<tr/>').append $('<td/>').text(flatId)
                row.attr 'contenteditable', true
                $table.append row
                loadFlatDetails row

    flatDetailsUrl = (flatId) ->
        $table.data('details').replace '0', flatId

    loadFlatDetails = (tableRow) ->
        flatId = tableRow.text()

        $.get flatDetailsUrl(flatId), (flat) ->
            tableRow.append $('<td/>').text(flat.userId)
            tableRow.append $('<td/>').text(flat.location)
            tableRow.append $('<td/>').text(flat.price)
            tableRow.append $('<td/>').text(flat.size)
            tableRow.append $('<td/>').text(flat.description)

    loadFlatTable()


    saveRow = ($row) ->
        [flatId, userId, location, price, size, description] = $row.children().map -> $(this).text()
        flat =
            flatId: parseInt(flatId)
            userId: parseInt(userId)
            location: location
            price: parseInt(price)
            size: parseInt(size)
            description: description
        jqxhr = $.ajax
            type: "PUT"
            url: flatDetailsUrl(flatId)
            contentType: "application/json"
            data: JSON.stringify flat
        jqxhr.done (response) ->
            $label = $('<span/>').addClass('label label-success')
            $row.children().last().append $label.text(response)
            $label.delay(3000).fadeOut()
        jqxhr.fail (data) ->
            $label = $('<span/>').addClass('label label-important')
            message = data.responseText || data.statusText
            $row.children().last().append $label.text(message)

    $table.on 'focusout', 'tr', () ->
        saveRow $(this)
