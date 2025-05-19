function startTourCustomer() {
    console.log("Chạy tour hướng dẫn bằng Intro.js");
	
	introJs().start();

//    const steps = [];
//
//    document.querySelectorAll('[data-tour]').forEach((el, index) => {
//        const title = el.getAttribute('data-tour-title') || `Bước ${index + 1}`;
//        const content = el.getAttribute('data-tour-content') || '';
//        steps.push({
//            element: el,
//            intro: `<b>${title}</b><br>${content}`,
//            position: 'bottom'
//        });
//    });
//
//    if (steps.length > 0) {
//        introJs().setOptions({
//            steps: steps,
//            showProgress: true,
//            showBullets: false,
//            nextLabel: 'Tiếp',
//            prevLabel: 'Lùi',
//            doneLabel: 'Xong',
//            skipLabel: 'Bỏ qua'
//        }).start();
//    } else {
//        console.warn("Không tìm thấy bước nào để chạy tour.");
//    }
}
