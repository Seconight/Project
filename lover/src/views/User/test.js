var axios = require('axios');
var FormData = require('form-data');
var fs = require('fs');
var data = new FormData();
data.append('faceImage', fs.createReadStream('2.jpg'));
data.append('studentId', '0121810880204');

var config = {
  method: 'post',
  url: 'http://localhost:8081/user/faceInfo',
  headers: { 
    ...data.getHeaders()
  },
  data : data
};

axios(config)
.then(function (response) {
  console.log(JSON.stringify(response.data));
})
.catch(function (error) {
  console.log(error);
});
