//表单验证
$(document).ready(function() {
    formValidator();
});
//Modal验证销毁重构
$('#myModal').on('hidden.bs.modal', function() {
    $("#addForm").data('bootstrapValidator').destroy();
    $('#addForm').data('bootstrapValidator', null);
    formValidator();
});
function formValidator(){
    $('form').bootstrapValidator({
        message: 'This value is not valid',
        excluded: [":disabled"],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
        	ruleFrequency: {
        		message: '验证失败',
        		validators: {
                    notEmpty: {
                        message: '不能为空'
                    }
                }
        	},
        	ruleName: {
                message: '验证失败',
                validators: {
                    notEmpty: {
                        message: '不能为空'
                    }
                }
            },
            ruleType: {
                message: '验证失败',
                validators: {
                    notEmpty: {
                        message: '不能为空'
                    }
                }
            },
            ruleSubType: {
                message: '验证失败',
                validators: {
                    notEmpty: {
                        message: '不能为空'
                    }
                }
            },
            warnLevel: {
            	message: '验证失败',
                validators: {
                    notEmpty: {
                        message: '不能为空'
                    }
                }
            },
            trigDesc: {
            	message: '验证失败',
                validators: {
                    notEmpty: {
                        message: '不能为空'
                    }
                }
            }
        }
    });
};


